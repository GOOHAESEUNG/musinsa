package com.sideproject.musinsa_backend.Common.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.security.Key;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

//클라이언트가 보낸 JWT 토큰을 검증해서, 이 사람이 ‘로그인한 사용자’라는 걸 스프링 시큐리티에 알려주기 위해서 작성
@Component
public class JwtAuthFilter extends GenericFilter {

    @Value("${JWT_SECRET_KEY}")
    private String secretKey;

    private Key key;

    @PostConstruct
    public void initKey() {
        this.key = new SecretKeySpec(
            Base64.getDecoder().decode(secretKey),
            SignatureAlgorithm.HS512.getJcaName()
        );
    }

    @Override
    public void doFilter(ServletRequest request, //request 안에 토큰이 들어가 있음
                         ServletResponse response, //토큰에 문제가 있으면 response에 반환해줌, 아니면 정상적으로 dofilter로 이동
                         FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        String token = httpServletRequest.getHeader("Authorization");

        try {

            if (token != null) { //토큰이 있는 경우에만 검증하겠다.
                if (!token.substring(0, 7).equals("Bearer ")) { //Bearer이 앞에 붙어있는지 확인
                    throw new AuthenticationServiceException("Bearer 형식이 아닙니다.");
                }
                String jwtToken = token.substring(7);//Bearer 떄내고 토큰의 원본만 꺼냄

                //우리 서비스 토큰인지 확인하는 방법은 시그니처 부분을 확인하는것

                //헤더 + 페이로드 + 시크릿키 암호화 -> 시그니처 부분
                //우리가 헤더 + 페이로드는 알고 있으니 암호화해서 동일한 내용인지 확인
                //claims은 페이로드 부분 이므로  아래는 현재 페이로드 부분을 꺼내는 것

                //토큰 검증 및 claims 추출
                Claims claims = Jwts.parserBuilder() //이 객체가 토큰을 검증하고 해석하는 기능을 가짐
                        .setSigningKey(key) //JWT의 서명을 검증할 때 사용할 시크릿 키를 설정함 -> 위조 여부 검증
                        .build() //파서를 실제로 완성시킴
                        .parseClaimsJws(jwtToken) //토큰을 해석함
                        //내부적으로 다음을 수행함
                        //1. 토큰 형식이 맞는지 확인
                        //2. 서명이 요한지 확인(위조 여부 판단)
                        //3. 유효하면 payload 안에 잇는 정보를 꺼냄
                        .getBody(); //위에서 해석한 토큰의 페이로드부분(claims)를 꺼냄
                //이 안에 우리가 넣은 정보가 들어있음(email, position, exp, iat 등)

//                JWT 인증 필터 안에서 Spring Security 인증 객체를 직접 만드는 과정


                //authentication 객체 생성

                //1. 권한 목록 생성
                List<GrantedAuthority> authorities = new ArrayList<>();
                authorities.add(new SimpleGrantedAuthority("POSITION_" + claims.get("position").toString()));
                //GrantedAuthority는 Spring Security에서 권한을 의미함
                //JWT에 담겨 있는 포지션 정보를 꺼내와서
                //예를 들어 매니저이면 "POSITION_MANAGER" 형태로 권한 부여



                UserDetails userDetails = new User(claims.getSubject(), "", authorities);
                //스프링 시큐리티의 기본 User 객체를 활용해서 UserDetails를 만듦
                //claims.getSubject() -> 일반적으로 이메일 또는 유저 ID로 저장됨
                //비밀번호는 여기선 사용하지 않기 때문에 문자열 "" 사용
                //아까 만든 권한 리스트를 여기 포함 시킴
                //이 유저 디테일스는 인증된 사용자 정보를 담고 있음



                Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
                //이 줄에서 실제로 인증 객체를 생성함
                //첫번째 인자 : 사용자 정보(userDetails)
                //두번째 인자 : 자격 증명(여기선 사용하지 않으므로 "")
                //세번째 인자 : 권한 목록(userDetails.getAuthorities())
                //이 인증 객체는 스프링 시큐리티가 인가 처리에 사용하는 핵심 객체임


                SecurityContextHolder.getContext().setAuthentication(authentication); //계층 구조
                //스프링 시큐리티의 보안 컨텍스트에 지금 만든 인증 객체를 등록함

            }
            chain.doFilter(request, response);
        }catch (Exception e) {
            e.printStackTrace();
            httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
            httpServletResponse.setContentType("application/json");
            httpServletResponse.getWriter().write("invalid token");
        }
    }
}
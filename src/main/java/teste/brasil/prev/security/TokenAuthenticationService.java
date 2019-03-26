package teste.brasil.prev.security;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class TokenAuthenticationService {

	static final long EXPIRATION_TIME = 860_000_000;
	static final String SECRET = "TesteBrasilPrev";
	static final String TOKEN_PREFIX = "Bearer";
	static final String HEADER_STRING = "Authorization";

	public static void addAuthentication(HttpServletResponse response, Authentication auth) {
		JwtBuilder builder = Jwts.builder();
		
		String roles = auth.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(","));
		
		builder.setSubject(auth.getName());
		builder.claim("roles", roles);
		builder.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME));
		
		String JWT = builder.signWith(SignatureAlgorithm.HS512, SECRET).compact();
		
		response.addHeader(HEADER_STRING, TOKEN_PREFIX + " " + JWT);
	}

	public static Authentication getAuthentication(HttpServletRequest request) {
		String token = request.getHeader(HEADER_STRING);

		if (token != null) {
			JwtParser jwtParser = Jwts.parser().setSigningKey(SECRET);

	        Jws<Claims> claimsJws = jwtParser.parseClaimsJws(token.replace(TOKEN_PREFIX, ""));

	        Claims claims = claimsJws.getBody();
			
			String email = claims.getSubject();

			if (email != null) {
		        List<SimpleGrantedAuthority> authorities = Arrays.stream(claims.get("roles").toString().split(",")).map(SimpleGrantedAuthority::new).collect(Collectors.toList());
				
				return new UsernamePasswordAuthenticationToken(email, null, authorities);
			}
		}

		return null;
	}

}
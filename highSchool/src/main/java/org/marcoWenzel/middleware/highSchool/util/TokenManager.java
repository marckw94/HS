package org.marcoWenzel.middleware.highSchool.util;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

public class TokenManager {
	
	public TokenManager(){
		
	}
	
	// dato username e tipologia di utente(categoria) crea il token
	public String issueToken(String username, String category){
		
		//settaggi per i claim temporali nel payload
		Calendar calendar = Calendar.getInstance();
		Date now = calendar.getTime();
		//valido per un'ora
		calendar.add(Calendar.HOUR, 1);
		Date expirationDate = calendar.getTime();
		
		//header (tipologia: jwt e algoritmo che viene aggiunto sotto)
		Map<String, Object> headerClaims = new HashMap<>();
		headerClaims.put("typ", "JWT");
		
		try {
			//algoritmo
			Algorithm algorithm = Algorithm.HMAC256("secret");
			
			//compongo il token
			String token = JWT.create().withSubject(username)
					   .withExpiresAt(expirationDate)
					   .withIssuer("prova")
					   .withIssuedAt(now)
					   .withNotBefore(now)
					   .withClaim("username", username)
					   .withClaim("category", category) //questo è il claim creato ad hoc per la categoria utente
					   .withHeader(headerClaims)
					   .sign(algorithm);
			
			
			/* 
			 * Questo serve solo per far vedere come si decodifica un token.
			 * Quindi praticamente, in seguito ad una richiesta del client, ci viene mandato il token
			 * che noi andiamo a decodificare. Una volta decodificato (DecodedJWT), esistono una serie di metodi per 
			 * recuperare il valore di un certo claim. Per i claim creati ad hoc da noi (come per esempio categoria) 
			 * basta usare getClaim("nome claim"). Questo valore è quello che poi andremo a usare come "role" 
			 * nelle annotazioni per verificare chi può accedere a determinate risorse e metodi
			 * 
			 * */
			
			DecodedJWT jwt = JWT.decode(token);
			
			return token;
		} catch (JWTVerificationException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
		
	}
	
	
	public static String validateToken(String token){
		DecodedJWT jwt = JWT.decode(token);
		String categoryInInput= jwt.getClaim("category").asString();
		
		return categoryInInput;
	}
	public static String validateUserToken(String token){
		DecodedJWT jwt = JWT.decode(token);
		String userInInput= jwt.getClaim("username").asString();
		
		return userInInput;
	}
	
}


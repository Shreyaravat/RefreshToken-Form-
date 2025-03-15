package com.project.config;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {
    
    private final SecretKey secretKey = Keys.hmacShaKeyFor("afafasfafafasfasfasfafacasdasfasxASFACASDFACASDFASFASFDAFASFASDAADSCSDFADCVSGCFVADXCcadwavfsfarvf".getBytes());       

    // Generate token
//    public String generateToken(String username) {
//        return Jwts.builder()
//                .setSubject(username)
//                .setIssuedAt(new Date(System.currentTimeMillis()))
//                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1 hour expiry
//                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
//                .compact();
//    }
    
//    public String generateToken(String username) {
//      return doGenerateToken(new HashMap<>(), username);
//  }
    public String generateToken(UserDetails userDetails)
    {
        Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims, userDetails.getUsername());
    }

    public String generateToken(String username) {
        return doGenerateToken(new HashMap<>(), username);
    }

    
    public String doGenerateToken(Map<String, Object> claims, String username) 
    {
    	return Jwts.builder()
				.claims()
				.add(claims)
				.subject(username)
				.issuedAt(new Date(System.currentTimeMillis()))
				.expiration(new Date(System.currentTimeMillis() + 1000 * 60))
				.and()
				.signWith(secretKey)
				.compact();
    }

    // Extract username from token
//    public String extractUsername(String token) 
//    {
//    	try 
//    	{
//            Claims claims = extractAllClaims(token);
//            System.out.println("Decoded Claims: " + claims); // Debugging line
//            String username = claims.getSubject();
//            System.out.println("Extracted Username from Token: " + username);
//            return username;
//        } 
//    	catch (Exception e) 
//    	{
//            System.out.println("Error while extracting username: " + e.getMessage());
//            return null;
//        }
//    }
    
    public String extractUsername(String token) 
    {
        Claims claims = extractAllClaims(token); 
        return claims.getSubject();
    }

    public Date extractIssuedAt(String token)
    {
        return extractClaim(token, Claims::getIssuedAt);
    }

    // Extract expiration date
    public Date extractExpiration(String token) 
    {
        return extractClaim(token, Claims::getExpiration);
    }

    // Extract specific claim
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) 
    {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

//    private Claims extractAllClaims(String token) {
//        return Jwts.parser()
//                .setSigningKey(secretKey)
//                .parseClaimsJws(token)
//                .getBody();
//    }
    private Claims extractAllClaims(String token) 
    {
    	return Jwts.parser()
				.verifyWith(secretKey)
				.build()
				.parseSignedClaims(token)
				.getPayload();
    }

    // Check if token is expired
    public boolean isTokenExpired(String token) 
    {
        return extractExpiration(token).before(new Date());
    }

    // Validate token
//    public boolean validateToken(String token, String username) {
//        return (extractUsername(token).equals(username) && !isTokenExpired(token));
//    }
    public boolean validateToken(String token, String expectedUsername) 
    {
        try 
        {
            String username = extractUsername(token);
            boolean isValid = (username.equals(expectedUsername) && !isTokenExpired(token));

            System.out.println("Validating Token...");
            System.out.println("Expected Username: " + expectedUsername);
            System.out.println("Extracted Username: " + username);
            System.out.println("Is Token Expired? " + isTokenExpired(token));
            System.out.println("Is Token Valid? " + isValid);

            return isValid;
        } 
        catch (Exception e) 
        {
            System.out.println("Token validation failed: " + e.getMessage());
            return false;
        }
    }

    
}

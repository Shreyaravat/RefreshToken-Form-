package com.project.service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.model.Login;
import com.project.model.RefreshToken;
import com.project.repo.RefreshTokenRepo;
import com.project.repo.RepoClass;

import jakarta.transaction.Transactional;

@Service 
public class RefreshTokenService 
{
	@Autowired
	private RefreshTokenRepo rtr;
	
	@Autowired
	private RepoClass rp; 
		
	public RefreshToken createRefreshToken(String name)
	{
	    // Fetch user from repository
	    Login login = rp.findByName(name)
	                   .orElseThrow(() -> new RuntimeException("User not found with name: " + name));

	    // Check if a refresh token already exists for this user
	    Optional<RefreshToken> existingToken = rtr.findByLogin(login);
	    
	    RefreshToken refreshToken;
	    
	    if (existingToken.isPresent()) {
	        refreshToken = existingToken.get();

	        // Check if the existing token is expired
	        if (refreshToken.getExpiry().isBefore(Instant.now())) 
	        {
	            // If expired, generate a new token
	            refreshToken.setRefreshToken(UUID.randomUUID().toString());
	        }

	        // Extend the expiry time by 30 minutes
	        refreshToken.setExpiry(Instant.now().plusMillis(30 * 60 * 1000));
	    }
	    else 
	    {
	        // Create a new refresh token
	        refreshToken = new RefreshToken();
	        refreshToken.setRefreshToken(UUID.randomUUID().toString());
	        refreshToken.setExpiry(Instant.now().plusMillis(30 * 60 * 1000));
	        refreshToken.setLogin(login);
	    }

	    return rtr.save(refreshToken); // Save and return updated token
	}

	
	public RefreshToken verifyRefreshToken(String refreshToken)
	{
		System.out.println("Verification");
		RefreshToken refreshToken2 = rtr.findByRefreshToken(refreshToken).orElseThrow(() -> new RuntimeException("Token does not exists!"));          
		
		if(refreshToken2.getExpiry().compareTo(Instant.now())<0)
		{
			throw new RuntimeException("Refresh token expired!!");
		}		
		return refreshToken2;
		
	}
}















//public RefreshToken createRefreshToken(String name)
//{
//	
//	Login login = rp.findByName(name).get();
////	RefreshToken rt = login.getRefreshToken();
//	
//    RefreshToken refreshToken = new RefreshToken();
//    
//    refreshToken.setRefreshToken(UUID.randomUUID().toString());
//    refreshToken.setExpiry(Instant.now().plusMillis(30 * 60 * 1000));
//    
//    // Fetch user from repository and set it in RefreshToken
//    Optional<Login> userOptional = rp.findByName(name);
//    if (userOptional.isPresent()) 
//    {
//        refreshToken.setLogin(userOptional.get());
//    } 
//    else 
//    {
//        throw new RuntimeException("User not found with name: " + name);
//    }
//
//    //save to database
//    rtr.save(refreshToken);
//    		
//    return refreshToken;
//}
//	

//public RefreshToken createRefreshToken(String name)
//{
//	RefreshToken refreshToken = RefreshToken.builder
//	.refreshToken(UUID.randomUUID().toString())
//	.expiry(Instant.now().plusMillis( 30*60*1000));
//	.user(rp.findByName(name).get())
//	.build();
//	return refreshToken;
//}

//@Transactional





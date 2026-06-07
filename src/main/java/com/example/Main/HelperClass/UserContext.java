package com.example.Main.HelperClass;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;

public class UserContext {
	
	
	 private static Authentication getAuth() {
	        return SecurityContextHolder.getContext().getAuthentication();
	    }

	 
	 
//	 public String getCurrentJti() {
//		  Authentication auth = getAuth();
//		  if (auth == null)  return null;
//		 
//		  if (auth.getDetails()) {
//			
//		}
		
//	}
	 
	    public static String getEmail() {
	        Authentication auth = getAuth();
	        return auth != null ? auth.getName() : null;
	    }
	    

	    public static String getRole() {
	        Authentication auth = getAuth();
	        if (auth == null || auth.getAuthorities().isEmpty()) {
	            return null;
	        }
	        return auth.getAuthorities().iterator().next().getAuthority();
	    }

	    public static Long getUserId() {
	        ServletRequestAttributes attr =
	                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

	        if (attr == null) return null;

	        HttpServletRequest request = attr.getRequest();
	        Object userId = request.getAttribute("userId");

	        return userId != null ? (Long) userId : null;
	    }

	    public static boolean isAdmin() {
	        String role = getRole();
	        return "ROLE_ADMIN".equals(role);
	    }
	

}

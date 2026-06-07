package com.example.Main.Security.RateLimit;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Synchronized;

@Component
//@Order(Ordered.HIGHEST_PRECEDENCE)
public class RateLimitingFilter extends OncePerRequestFilter {

	Map<String, AttemptInfo> attempts = new ConcurrentHashMap<String, AttemptInfo>();

	private static final int MAX_ATTEMPTS = 5;
	private static final long WINDOW_MS = 60_000;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String path = request.getServletPath();

		if (path.startsWith("/api/v1/auth/login") || path.startsWith("/api/v1/auth/refresh")) {

			String ip = request.getHeader("X-Forwarded-For");
			if (ip != null && !ip.isEmpty()) {
				ip = ip.split(",")[0].trim();

			} else {
				ip = request.getRemoteAddr();
			}

			Long now = System.currentTimeMillis();

			AttemptInfo info = attempts.computeIfAbsent(ip, k -> new AttemptInfo(0, now));

//			if (!attempts.containsKey(ip)) {
//				
//				attempts.put(ip, new AttemptInfo(0, now));
//				
//			}
			
//			 AttemptInfo info = attempts.get(ip);
			
			
			synchronized (info) {

				if (now - info.windowStartMillis > WINDOW_MS) {

					info.count = 0;
					info.windowStartMillis = now;

				}

				info.count++;

				if (info.count > MAX_ATTEMPTS) {
					response.setStatus(HttpServletResponse.SC_REQUEST_ENTITY_TOO_LARGE);
					response.getWriter().write("Too many requests. Please try again later.");
					return;
				}

			}

		}

		filterChain.doFilter(request, response);

//		String Ip = request.getRemoteAddr();
//		Map<Ip, AttemptInfo> rateLimit = new HashMap<K, V>();

	}

	private static class AttemptInfo {

		int count;
		long windowStartMillis;

		public AttemptInfo(int count, long windowStartMillis) {

			this.count = count;
			this.windowStartMillis = windowStartMillis;
		}

	}
}
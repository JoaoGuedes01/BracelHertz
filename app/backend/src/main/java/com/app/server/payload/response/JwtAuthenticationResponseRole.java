package com.app.server.payload.response;

public class JwtAuthenticationResponseRole {
	private String accessToken;
	private String tokenType = "Bearer";
	private String role;
	private Long userId;

	public JwtAuthenticationResponseRole(String accessToken, String role) {
		this.accessToken = accessToken;
		this.role = role;
	}

	public JwtAuthenticationResponseRole(String accessToken, String role, Long userId) {
		this.accessToken = accessToken;
		this.role = role;
		this.userId = userId;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getTokenType() {
		return tokenType;
	}

	public void setTokenType(String tokenType) {
		this.tokenType = tokenType;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

}
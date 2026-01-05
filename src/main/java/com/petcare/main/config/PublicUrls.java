package com.petcare.main.config;

public final class PublicUrls {
	
	public static final String[] USER_PUBLIC_URLS = {
			
			"/user/registerPage",
			"/user/registerUser",
			"/user/loginPage",
			"/user/verify",
			"/user/verification-result",
			"/user/forgot-password",
			"/user/searchAccount",
			"/user/send-reset-link",
			"/user/resetPassword",
			"/user/reset-password",
			"/css/**",
			"/js/**",
			"/images/**"
	};
	
	public static final String[] ADMIN_PUBLIC_URLS = {
			"/admin",
			"/admin/register",
			"/admin/registerAdmin",
			"/admin/loginPage",
			"/admin/forgot-password",
			"/admin/resetPassword",
			"/css/**",
			"/js/**",
			"/images/**"
	};
	
	public static final String[] HOME_PUBLIC_URLS = {
			"/",
			
			"/index",
			"/css/**",
			"/js/**",
			"/images/**"
	};
	public static final String[] VET_PUBLIC_URLS = {
			"/vet/loginPage",
			"/css/**",
			"/js/**",
			"/images/**"
	};

}

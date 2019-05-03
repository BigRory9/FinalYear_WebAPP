//package com.roryharford.user.security;
//
//import org.jboss.logging.Logger;
//import org.springframework.context.ApplicationListener;
//import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
//import org.springframework.stereotype.Component;
//
//@Component
//public class MyApplicationListener implements ApplicationListener<AuthenticationFailureBadCredentialsEvent> {
//    private static final Logger LOG = Logger.getLogger(MyApplicationListener.class);
//
//    @Override
//    public void onApplicationEvent(AuthenticationFailureBadCredentialsEvent event) {
//        Object userName = event.getAuthentication().getPrincipal();
//        Object credentials = event.getAuthentication().getCredentials();
//        System.out.println("Failed login using USERNAME [" + userName + "]");
//        System.out.println("Failed login using PASSWORD [" + credentials + "]");
//    }
//}
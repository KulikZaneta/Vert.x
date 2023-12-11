package com.betacom.vertx_project.auth;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordHasher {

  public static String hashPassword(String plainPassword) {
    return BCrypt.hashpw(plainPassword, BCrypt.gensalt());
  }

  public static boolean verifyPassword(String plainPassword, String hashedPassword) {
    return BCrypt.checkpw(plainPassword, hashedPassword);
  }
}
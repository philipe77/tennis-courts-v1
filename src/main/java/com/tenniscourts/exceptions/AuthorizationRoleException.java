package com.tenniscourts.exceptions;

/**
 * The type Already exists entity exception.
 */
public class AuthorizationRoleException extends RuntimeException {
  /**
   * Instantiates a new Already exists entity exception.
   *
   * @param msg the msg
   */
  public AuthorizationRoleException(String msg){
        super(msg);
    }

    private AuthorizationRoleException(){}
}

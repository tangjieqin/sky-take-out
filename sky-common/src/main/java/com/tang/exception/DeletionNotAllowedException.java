package com.tang.exception;

public class DeletionNotAllowedException extends BaseException{
    public DeletionNotAllowedException() {}
    public DeletionNotAllowedException(String mesg) {
        super(mesg);
    }

}

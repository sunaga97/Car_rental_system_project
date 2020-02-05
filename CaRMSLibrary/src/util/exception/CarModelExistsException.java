/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.exception;

/**
 *
 * @author sunag
 */
public class CarModelExistsException extends Exception {

    /**
     * Creates a new instance of <code>CarModelExistsException</code> without
     * detail message.
     */
    public CarModelExistsException() {
    }

    /**
     * Constructs an instance of <code>CarModelExistsException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public CarModelExistsException(String msg) {
        super(msg);
    }
}

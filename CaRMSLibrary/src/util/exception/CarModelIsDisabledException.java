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
public class CarModelIsDisabledException extends Exception {

    /**
     * Creates a new instance of <code>CarModelIsDisabledException</code>
     * without detail message.
     */
    public CarModelIsDisabledException() {
    }

    /**
     * Constructs an instance of <code>CarModelIsDisabledException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public CarModelIsDisabledException(String msg) {
        super(msg);
    }
}

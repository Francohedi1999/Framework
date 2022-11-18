/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

/**
 *
 * @author Franco
 */
public class PackageNotExist extends Exception {

    public PackageNotExist() {
    }

    public PackageNotExist(String erreur) {
        super(erreur);
    }
}

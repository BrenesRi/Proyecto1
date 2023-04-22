/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyecto1.logic;

/**
 *
 * @author ribre
 */
public class Modelo {
    Integer id;
    String descripcion;
    Marca marca;
    Integer marcaId;

    public Modelo(String descripcion, Marca marca) {
        this.descripcion = descripcion;
        this.marca = marca;
        this.marcaId = 0;
    }
    
    public Modelo(String descripcion, Marca marca, Integer marcaId) {
        this.descripcion = descripcion;
        this.marca = marca;
        this.marcaId = marcaId;
    }

    public Modelo() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Marca getMarca() {
        return marca;
    }

    public void setMarca(Marca marca) {
        this.marca = marca;
    }

    public Integer getMarcaId() {
        return marcaId;
    }

    public void setMarcaId(Integer marcaId) {
        this.marcaId = marcaId;
    }
    
    @Override
    public String toString() {
        return "Modelo{" + "id=" + id + ", descripcion=" + descripcion + ", marca=" + marca + '}';
    }
  
}

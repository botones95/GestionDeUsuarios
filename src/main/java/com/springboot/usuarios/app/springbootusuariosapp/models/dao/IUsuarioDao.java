package com.springboot.usuarios.app.springbootusuariosapp.models.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.springboot.usuarios.app.springbootusuariosapp.models.entity.Usuario;

public interface IUsuarioDao extends CrudRepository<Usuario, Long>, PagingAndSortingRepository<Usuario, Long> {

}

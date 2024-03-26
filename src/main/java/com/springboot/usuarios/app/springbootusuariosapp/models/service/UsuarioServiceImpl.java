package com.springboot.usuarios.app.springbootusuariosapp.models.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springboot.usuarios.app.springbootusuariosapp.models.dao.IUsuarioDao;
import com.springboot.usuarios.app.springbootusuariosapp.models.entity.Usuario;

@Service
public class UsuarioServiceImpl implements IUsuarioService {

    @Autowired
    private IUsuarioDao usuarioDao;

    @Transactional(readOnly = true)
    @Override
    public List<Usuario> findAll() {
        return (List<Usuario>) usuarioDao.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public Usuario findOne(Long id) {
        return usuarioDao.findById(id).orElse(null);
    }

    @Transactional
    @Override
    public void save(Usuario usuario) {
        usuarioDao.save(usuario);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        usuarioDao.deleteById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<Usuario> findAll(Pageable pageable) {
        return usuarioDao.findAll(pageable);
    }

}

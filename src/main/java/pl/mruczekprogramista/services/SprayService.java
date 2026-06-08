package pl.mruczekprogramista.services;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import pl.mruczekprogramista.data.Spray;
import pl.mruczekprogramista.data.SprayRepository;


import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class SprayService {

    private final SprayRepository repository;

    public SprayService(SprayRepository repository) {
        this.repository = repository;
    }

    public Optional<Spray> get(Integer id) {
        return repository.findById(id);
    }

    public Spray save(Spray entity) {
        return repository.save(entity);
    }

    public void deleteByid(Long id) {
        repository.deleteById(id);

    }

    public Page<Spray> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Page<Spray> list(Pageable pageable, Specification<Spray> filter) {
        return repository.findAll(filter, pageable);
    }

    public int count() {
        return (int) repository.count();
    }

    public List<Spray> findAll() {
        return repository.findAll();
    }
}

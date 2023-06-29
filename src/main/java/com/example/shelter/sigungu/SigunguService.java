package com.example.shelter.sigungu;

import com.example.shelter.exception.notfound.SigunguNotFoundException;
import com.example.shelter.sido.Sido;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SigunguService {

    private final SigunguRepository sigunguRepository;

    public List<Sigungu> findAll() {
        return sigunguRepository.findAllNotDeleted(Sort.by(Sort.Direction.ASC, "name"));
    }

    public List<Sigungu> findAllBySido(Sido sido) {
        return sigunguRepository.findAllBySidoNotDeleted(sido, Sort.by(Sort.Direction.ASC, "name"));
    }

    public Sigungu findById(Long id) {
        return sigunguRepository.findByIdNotDeleted(id)
                .orElseThrow(() -> new SigunguNotFoundException(id));
    }

    @Transactional
    public Long save(Sigungu sigungu) {
        return sigunguRepository.save(sigungu).getId();
    }

    @Transactional
    public void saveAll(List<Sigungu> sigungus) {
        sigunguRepository.saveAll(sigungus);
    }

    @Transactional
    public void updateName(Long id, String name) {
        Sigungu sigungu = sigunguRepository.findByIdNotDeleted(id)
                .orElseThrow(() -> new SigunguNotFoundException(id));
        sigungu.updateName(name);
    }

    @Transactional
    public void updateSido(Long id, Sido sido) {
        Sigungu sigungu = sigunguRepository.findByIdNotDeleted(id)
                .orElseThrow(() -> new SigunguNotFoundException(id));
        sigungu.updateSido(sido);
    }

    @Transactional
    public void delete(Long id) {
        Sigungu sigungu = sigunguRepository.findByIdNotDeleted(id)
                .orElseThrow(() -> new SigunguNotFoundException(id));
        sigungu.updateDeleted(true);
    }

}

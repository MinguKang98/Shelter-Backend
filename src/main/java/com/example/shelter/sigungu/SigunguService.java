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
        return sigunguRepository.findAll(Sort.by(Sort.Direction.ASC, "name"));
    }

    public List<Sigungu> findAllBySido(Sido sido) {
        return sigunguRepository.findAllBySido(sido, Sort.by(Sort.Direction.ASC, "name"));
    }

    public Sigungu findById(Long id) {
        return sigunguRepository.findById(id).orElseThrow(SigunguNotFoundException::new);
    }

    @Transactional
    public void updateName(Long id, String name) {
        Sigungu sigungu = sigunguRepository.findById(id).orElseThrow(SigunguNotFoundException::new);
        sigungu.updateName(name);
    }

    @Transactional
    public void updateSido(Long id, Sido sio) {
        Sigungu sigungu = sigunguRepository.findById(id).orElseThrow(SigunguNotFoundException::new);
        sigungu.updateSido(sio);
    }

}

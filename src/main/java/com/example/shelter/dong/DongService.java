package com.example.shelter.dong;

import com.example.shelter.exception.notfound.DongNotFoundException;
import com.example.shelter.sigungu.Sigungu;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DongService {

    private final DongRepository dongRepository;

    public List<Dong> findAll() {
        return dongRepository.findAllNotDeleted(Sort.by(Sort.Direction.ASC, "name"));
    }

    public List<Dong> findAllBySigungu(Sigungu sigungu) {
        return dongRepository.findAllBySigunguNotDeleted(sigungu, Sort.by(Sort.Direction.ASC, "name"));
    }

    public Dong findById(Long id) {
        return dongRepository.findByIdNotDeleted(id)
                .orElseThrow(() -> new DongNotFoundException(id));
    }

    @Transactional
    public Long save(Dong dong) {
        return dongRepository.save(dong).getId();
    }

    @Transactional
    public void saveAll(List<Dong> dongs) {
        dongRepository.saveAll(dongs);
    }

    @Transactional
    public void updateName(Long id, String name) {
        Dong dong = dongRepository.findByIdNotDeleted(id)
                .orElseThrow(() -> new DongNotFoundException(id));
        dong.updateName(name);
    }

    @Transactional
    public void updateSigungu(Long id, Sigungu sigungu) {
        Dong dong = dongRepository.findByIdNotDeleted(id)
                .orElseThrow(() -> new DongNotFoundException(id));
        dong.updateSigungu(sigungu);
    }

    @Transactional
    public void delete(Long id) {
        Dong dong = dongRepository.findByIdNotDeleted(id)
                .orElseThrow(() -> new DongNotFoundException(id));
        dong.updateDeleted(true);
    }

}

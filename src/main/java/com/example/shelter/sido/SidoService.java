package com.example.shelter.sido;

import com.example.shelter.exception.duplicate.SidoNameDuplicateException;
import com.example.shelter.exception.notfound.SidoNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SidoService {

    private final SidoRepository sidoRepository;

    /**
     * 모든 Sido 를 이름의 오름차순으로 정렬하여 반환
     *
     * @return 모든 Sido
     */
    public List<Sido> findAll() {
        return sidoRepository.findAllNotDeleted(Sort.by(Sort.Direction.ASC, "name"));
    }

    /**
     * 특정 id 를 가진 Sido 를 반환. 없다면 SidoNotFoundException 을 throw
     *
     * @param id 찾는 Sido 의 id
     * @return id 를 가진 Sido
     * @throws SidoNotFoundException id 를 가진 Sido 가 없을 때
     */
    public Sido findById(Long id) {
        return sidoRepository.findByIdNotDeleted(id).orElseThrow(() -> new SidoNotFoundException(id));
    }

    /**
     * 특정 이름을 가진 Sido 를 반환. 없다면 SidoNotFoundException 을 throw
     *
     * @param name 찾는 Sido 의 이름
     * @return name 을 가진 Sido
     * @throws SidoNotFoundException name 을 가진 Sido 가 없을 때
     */
    public Sido findByName(String name) {
        return sidoRepository.findByNameNotDeleted(name).orElseThrow(() -> new SidoNotFoundException(name));
    }

    public Long save(Sido sido) {
        return sidoRepository.save(sido).getId();
    }

    public void saveAll(List<Sido> sidos) {
        sidoRepository.saveAll(sidos);
    }

    /**
     * 특정 id 를 가진 Sido 의 이름을 변경. 없다면 SidoNotFoundException 을 throw
     *
     * @param id   변경할 Sido 의 id
     * @param name 변경할 이름
     * @throws SidoNotFoundException id 를 가진 Sido 가 없을 때
     */
    @Transactional
    public void updateName(Long id, String name) {
        Sido sido = sidoRepository.findByIdNotDeleted(id).orElseThrow(() -> new SidoNotFoundException(id));
        if (sidoRepository.findByNameNotDeleted(name).isPresent()) {
            throw new SidoNameDuplicateException(name);
        }
        sido.updateName(name);
    }

    @Transactional
    public void delete(Long id) {
        Sido sido = sidoRepository.findByIdNotDeleted(id).orElseThrow(() -> new SidoNotFoundException(id));
        sido.updateDeleted(true);
    }

}

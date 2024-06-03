package app.repository;

import app.domain.Code;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CodeRepository extends JpaRepository<Code, Long> {
    public List<Code> findAllByTimeBefore(LocalDateTime time);
    public Code findByCode(String code);
}

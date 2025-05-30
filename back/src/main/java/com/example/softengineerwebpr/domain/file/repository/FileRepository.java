package com.example.softengineerwebpr.domain.file.repository;

import com.example.softengineerwebpr.domain.file.entity.File;
import com.example.softengineerwebpr.domain.file.entity.FileReferenceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FileRepository extends JpaRepository<File, Long> {

    /**
     * 특정 참조 타입과 참조 ID에 해당하는 모든 파일 목록을 조회합니다.
     * 예를 들어, 특정 게시글(referenceType=POST, referenceIdx=postId)에 첨부된 모든 파일을 가져옵니다.
     *
     * @param referenceType 파일이 첨부된 대상의 유형 (POST, COMMENT, TASK 등)
     * @param referenceIdx  참조 대상의 ID
     * @return 해당 조건에 맞는 파일 엔티티 목록
     */
    List<File> findByReferenceTypeAndReferenceIdxOrderByUploadedAtAsc(FileReferenceType referenceType, Long referenceIdx);

    /**
     * 파일의 고유 경로 또는 식별자로 파일을 조회합니다. (예: 다운로드 시 사용)
     * filePath가 시스템 내에서 유니크하다고 가정할 경우 유용합니다.
     *
     * @param filePath 서버에 저장된 파일 경로 또는 유니크한 파일 키
     * @return Optional<File> 객체
     */
    Optional<File> findByFilePath(String filePath);

    // 필요에 따라 추가적인 조회 메소드 정의 가능
    // 예: 특정 사용자가 업로드한 모든 파일 목록 조회
    // List<File> findByUploadedBy(User uploadedBy);
}
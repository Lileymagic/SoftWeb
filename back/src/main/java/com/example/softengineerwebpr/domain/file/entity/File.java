package com.example.softengineerwebpr.domain.file.entity;

import com.example.softengineerwebpr.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class) // @CreatedDate 사용 위함
@Table(name = "file")
public class File {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idx")
    private Long idx;

    @Enumerated(EnumType.STRING)
    @Column(name = "reference_type", nullable = false, length = 10) // DB ENUM('post', 'comment', 'task') 길이 고려
    private FileReferenceType referenceType;

    @Column(name = "reference_idx", nullable = false)
    private Long referenceIdx; // 참조하는 대상의 ID (post_idx, comment_idx, task_idx)

    @Column(name = "file_name", nullable = false, length = 255)
    private String fileName; // 원본 파일명

    @Column(name = "file_path", nullable = false, length = 500)
    private String filePath; // 서버에 저장된 경로 또는 파일 키

    @Column(name = "file_size") // DB 스키마상 INT NULL [cite: 29]
    private Integer fileSize; // 파일 크기 (bytes)

    @CreatedDate
    @Column(name = "uploaded_at", nullable = false, updatable = false)
    private LocalDateTime uploadedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uploaded_by", nullable = false, foreignKey = @ForeignKey(name = "fk_file_user_uploaded_by"))
    private User uploadedBy; // 업로드한 사용자

    @Builder
    public File(FileReferenceType referenceType, Long referenceIdx, String fileName,
                String filePath, Integer fileSize, User uploadedBy) {
        this.referenceType = referenceType;
        this.referenceIdx = referenceIdx;
        this.fileName = fileName;
        this.filePath = filePath;
        this.fileSize = fileSize;
        this.uploadedBy = uploadedBy;
        // this.uploadedAt은 @CreatedDate에 의해 자동 설정
    }

    // 파일 정보 수정을 위한 메소드 (예: 파일명 변경 등, 필요시)
    // public void updateFileName(String newFileName) {
    //     this.fileName = newFileName;
    // }
}
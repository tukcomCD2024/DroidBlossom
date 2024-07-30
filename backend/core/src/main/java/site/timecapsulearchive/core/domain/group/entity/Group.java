package site.timecapsulearchive.core.domain.group.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import site.timecapsulearchive.core.domain.group.exception.GroupCreateException;
import site.timecapsulearchive.core.global.entity.BaseEntity;

@Entity
@Table(name = "group")
@Getter
@SQLDelete(sql = "UPDATE `group` SET deleted_at = now() WHERE group_id = ?")
@Where(clause = "deleted_at is null")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Group extends BaseEntity {

    @Id
    @Column(name = "group_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "group_name", nullable = false)
    private String groupName;

    @Column(name = "group_description", nullable = false)
    private String groupDescription;

    @Column(name = "group_profile_url", nullable = false)
    private String groupProfileUrl;

    @Builder
    private Group(String groupName, String groupDescription, String groupProfileUrl) {
        if (Objects.isNull(groupName)
            && Objects.isNull(groupDescription)
            && Objects.isNull(groupProfileUrl)
        ) {
            throw new GroupCreateException();
        }

        this.groupName = groupName;
        this.groupDescription = groupDescription;
        this.groupProfileUrl = groupProfileUrl;
    }

    public void updateGroupName(String groupName) {
        this.groupName = groupName;
    }

    public void updateGroupDescription(String groupDescription) {
        this.groupDescription = groupDescription;
    }

    public void updateGroupProfileUrl(String groupProfileUrl) {
        this.groupProfileUrl = groupProfileUrl;
    }
}

package org.example.fitnesspj.domain.workout;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.fitnesspj.domain.record.SetRecord;
import org.example.fitnesspj.domain.user.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class Workout {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;


    private LocalDate workoutDate;
    private String memo;


    @OneToMany(mappedBy = "workout", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SetRecord> setRecords = new ArrayList<>();


    public static Workout create(User user, LocalDate date, String memo) {
        Workout workout = new Workout();
        workout.user = user;
        workout.workoutDate = date;
        workout.memo = memo;
        return workout;
    }


    public void addSet(SetRecord setRecord) {
        setRecords.add(setRecord);
        setRecord.assignWorkout(this);
    }

}

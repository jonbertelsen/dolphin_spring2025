package app.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@ToString
@Getter
public class NotePersonDTO {
    private String note;
    private String personName;
    private int personAge;
}

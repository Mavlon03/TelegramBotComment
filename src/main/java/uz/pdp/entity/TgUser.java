package uz.pdp.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TgUser {
    private Long id;
    private String name;
    private TgState tgState = TgState.START;
    private User selectedUser;
    private Post selectedPost;
}

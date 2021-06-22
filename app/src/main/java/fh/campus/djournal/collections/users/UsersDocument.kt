package fh.campus.djournal.collections.users

import lombok.Getter
import lombok.Setter
import java.io.Serializable

@Getter
@Setter
class UsersDocument : Serializable{
    val email: String? = null
}
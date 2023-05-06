package practicum.Models;

public class UserPatchResponse {
    private boolean success;
    private CreatedUser user;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public CreatedUser getUser() {
        return user;
    }

    public void setUser(CreatedUser user) {
        this.user = user;
    }
}

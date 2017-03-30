package nz.ac.cornell.fitnessmealplans.Models;

public class Preference {

    private String codeId;
    private String codeName;
    private String description;

    public Preference(String codeId, String codeName, String description) {
        this.codeId = codeId;
        this.codeName = codeName;
        this.description = description;
    }

    public String getCodeId() {
        return this.codeId;
    }

    public void setCodeId(String codeId) {
        this.codeId = codeId;
    }

    public String getCodeName() {
        return this.codeName;
    }

    public void setCodeName(String codeName) {
        this.codeName = codeName;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

package tr.edu.boun.swe574.timeoutclient.jsonObjects;

/**
 * Created by haluks on 23/05/15.
 */
public class Tag {

        /*
        {"id":"Q1660056","url":"//www.wikidata.org/wiki/Q1660056","description":"male given name","label":"Alican"}
     */

    /*
                tag.tagName = selectedTags[i].searchString;
            tag.contextId = selectedTags[i].originalObject.id;
            tag.url = selectedTags[i].originalObject.url;
            if (selectedTags[i].originalObject.aliases != null) {
                tag.alias = selectedTags[i].originalObject.aliases[0];
            }
            tag.description = selectedTags[i].originalObject.description;
            tag.label = selectedTags[i].originalObject.label;
     */

    String id;
    String url;
    String description;
    String label;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}

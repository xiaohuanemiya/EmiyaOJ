// BaseFile.java
package com.emiyaoj.service.domain.pojo.oj.Model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.EXISTING_PROPERTY,
    property = "type"
)
@JsonSubTypes({
    @JsonSubTypes.Type(value = LocalFile.class, name = "local"),
    @JsonSubTypes.Type(value = MemoryFile.class, name = "memory"),
    @JsonSubTypes.Type(value = PreparedFile.class, name = "prepared"),
    @JsonSubTypes.Type(value = Collector.class, name = "collector"),
    @JsonSubTypes.Type(value = StreamIn.class, name = "streamIn"),
    @JsonSubTypes.Type(value = StreamOut.class, name = "streamOut"),
    @JsonSubTypes.Type(value = Symlink.class, name = "symlink")
})
@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class BaseFile {
    private String type;

}
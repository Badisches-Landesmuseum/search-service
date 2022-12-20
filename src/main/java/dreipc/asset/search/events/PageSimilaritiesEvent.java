package dreipc.asset.search.events;

import lombok.Getter;

public enum PageSimilaritiesEvent implements Event {
    DEFAULT("page.similarities"),
    CREATE(DEFAULT.getName() + ".created"),
    UPDATE(DEFAULT.getName() + ".updated"),
    DELETE(DEFAULT.getName() + ".deleted");

    @Getter
    private final String name;

    PageSimilaritiesEvent(String name) {
        this.name = name;
    }
}

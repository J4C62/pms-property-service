package com.github.j4c62.pms.property.domain.aggregate.vo;

/**
 * Value object representing the data attributes of a property.
 *
 * <p>This record encapsulates all relevant property attributes, such as name, type, capacity, size,
 * layout, isolation, accessibility, facilities, location, and price.
 *
 * @author Jose Antonio (J4c62)
 * @version 1.0.0
 * @since 2025-09-26
 */
public record Data(
    Name name,
    Type type,
    Capacity capacity,
    Size size,
    CeilingHeight ceilingHeight,
    LayoutType layout,
    IsolationLevel isolation,
    boolean privateEntrance,
    Window windows,
    int closedRooms,
    Floor floor,
    AccessibilityRating accessibility,
    Facilities facilities,
    Location location,
    Price price) {}

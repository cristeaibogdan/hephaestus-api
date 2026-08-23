package org.personal.washingmachine.usecase.user.getorganizationandcountry;

public record GetOrganizationAndCountryResponse(
        String organization,
        String country
) { }
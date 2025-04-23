package org.personal.solarpanel.mapper;

import org.personal.solarpanel.dto.GetSolarPanelFullResponse;
import org.personal.solarpanel.dto.SaveSolarPanelRequest;
import org.personal.solarpanel.dto.SearchSolarPanelResponse;
import org.personal.solarpanel.entity.Damage;
import org.personal.solarpanel.entity.SolarPanel;
import org.springframework.stereotype.Component;

@Component
public class SolarPanelMapper {

	public SolarPanel toEntity(SaveSolarPanelRequest dto) {
		return new SolarPanel(
				dto.category(),
				dto.manufacturer(),
				dto.model(),
				dto.type(),
				dto.serialNumber(),
				new Damage(
						dto.saveSolarPanelDamageRequest().hotSpots(),
						dto.saveSolarPanelDamageRequest().microCracks(),
						dto.saveSolarPanelDamageRequest().snailTrails(),
						dto.saveSolarPanelDamageRequest().brokenGlass(),
						dto.saveSolarPanelDamageRequest().additionalDetails()
				)
		);
	}

	public SearchSolarPanelResponse toSearchSolarPanelResponse(SolarPanel entity) {
		return new SearchSolarPanelResponse(
				entity.getCategory(),
				entity.getManufacturer(),
				entity.getModel(),
				entity.getType(),
				entity.getSerialNumber(),
				entity.getRecommendation(),
				entity.getCreatedAt()
		);
	}

	public GetSolarPanelFullResponse toGetSolarPanelFullResponse(SolarPanel entity) {
		return new GetSolarPanelFullResponse(
				entity.getCategory(),
				entity.getManufacturer(),
				entity.getModel(),
				entity.getType(),
				entity.getSerialNumber(),
				entity.getCreatedAt(),
				entity.getRecommendation()
		);
	}
}

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
						dto.damage().hotSpots(),
						dto.damage().microCracks(),
						dto.damage().snailTrails(),
						dto.damage().brokenGlass(),
						dto.damage().additionalDetails()
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
				entity.getRecommendation(),
				new GetSolarPanelFullResponse.DamageResponse(
						entity.getDamage().isHotSpots(),
						entity.getDamage().isMicroCracks(),
						entity.getDamage().isSnailTrails(),
						entity.getDamage().isBrokenGlass(),
						entity.getDamage().getAdditionalDetails()
				)
		);
	}
}

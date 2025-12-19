package com.iteminfocopier;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("iteminfocopier")
public interface ItemInfoCopierConfig extends Config
{
	@ConfigItem(
			keyName = "ENABLE_COPY_ID",
			name = "Enable copy item ID",
			description = "Check this box to enable the copy item ID feature."
	)
	default boolean enableCopyId()
	{
		return true;
	}

	@ConfigItem(
			keyName = "ENABLE_COPY_NAME",
			name = "Enable copy item name",
			description = "Check this box to enable the copy item name feature."
	)
	default boolean enableCopyName()
	{
		return true;
	}

	@ConfigItem(
			keyName = "REQUIRE_SHIFT",
			name = "Require shift key",
			description = "Check this box to require shift key for menu entries."
	)
	default boolean requireShift()
	{
		return true;
	}
}

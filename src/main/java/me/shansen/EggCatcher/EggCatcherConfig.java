package me.shansen.EggCatcher;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Collections;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

/**
 * Egg Catcher configuration container
 */
public class EggCatcherConfig {
    public final boolean usePermissions;
    public final boolean useCatchChance;
    public final boolean useHealthPercentage;
    public final boolean loseEggOnFail;
    public final boolean useVaultCost;
    public final boolean useItemCost;
    public final boolean explosionEffect;
    public final boolean smokeEffect;
    public final boolean nonPlayerCatching;
    public final boolean preventCatchingBabyAnimals;
    public final boolean preventCatchingTamedAnimals;
    public final boolean preventCatchingShearedSheeps;
    public final String vaultTargetBankAccount;
    public final boolean spawnChickenOnFail;
    public final boolean spawnChickenOnSuccess;
    public final boolean deleteVillagerInventoryOnCatch;
    public final boolean costOnChanceFail;
    public final boolean logCaptures;
    public final Map<String, Double> catchChances;
    public final Map<String, Double> vaultCosts;
    public final Map<String, Double> healthPercentages;
    public final Messages messages;
    public final ItemCost itemCost;

    public EggCatcherConfig(FileConfiguration config) {
        usePermissions = config.getBoolean("UsePermissions", true);
        useCatchChance = config.getBoolean("UseCatchChance", true);
        useHealthPercentage = config.getBoolean("UseHealthPercentage", false);
        loseEggOnFail = config.getBoolean("LooseEggOnFail", true);
        useVaultCost = config.getBoolean("UseVaultCost", false);
        useItemCost = config.getBoolean("UseItemCost", false);
        explosionEffect = config.getBoolean("ExplosionEffect", true);
        smokeEffect = config.getBoolean("SmokeEffect", false);
        nonPlayerCatching = config.getBoolean("NonPlayerCatching", true);
        preventCatchingBabyAnimals = config.getBoolean("PreventCatchingBabyAnimals", true);
        preventCatchingTamedAnimals = config.getBoolean("PreventCatchingTamedAnimals", true);
        preventCatchingShearedSheeps = config.getBoolean("PreventCatchingShearedSheeps", true);
        spawnChickenOnFail = config.getBoolean("SpawnChickenOnFail", true);
        spawnChickenOnSuccess = config.getBoolean("SpawnChickenOnSuccess", false);
        vaultTargetBankAccount = config.getString("VaultTargetBankAccount", "");
        deleteVillagerInventoryOnCatch = config.getBoolean("DeleteVillagerInventoryOnCatch", false);
        costOnChanceFail = config.getBoolean("CostOnChanceFail", false);
        logCaptures = config.getBoolean("LogEggCaptures", false);

        // config sub-sections
        ConfigurationSection catchChanceConfig = config.getConfigurationSection("CatchChance");
        ConfigurationSection vaultCostConfig = config.getConfigurationSection("VaultCost");
        ConfigurationSection healthPercentageConfig = config.getConfigurationSection("HealthPercentage");
        catchChances = configSectionToMap(catchChanceConfig, catchChanceConfig::getDouble, 0.0);
        vaultCosts = configSectionToMap(vaultCostConfig, vaultCostConfig::getDouble, 0.0);
        healthPercentages = configSectionToMap(healthPercentageConfig, healthPercentageConfig::getDouble, 0.0);
        messages = new Messages(config.getConfigurationSection("Messages"));
        itemCost = new ItemCost(config.getConfigurationSection("ItemCost"));
    }

    private static <T> Map<String, T> configSectionToMap(
            ConfigurationSection section,
            BiFunction<String, T, T> valueProvider,
            T defaultValue) {
        return Collections.unmodifiableMap(
                section.getKeys(false).stream()
                        .collect(Collectors.toMap(k -> k, k -> valueProvider.apply(k, defaultValue))));
    }

    /**
     * Messages config section
     */
    public static class Messages {
        public final String catchChanceSuccess;
        public final String catchChanceFail;
        public final String vaultSuccess;
        public final String vaultFail;
        public final String itemCostSuccess;
        public final String itemCostFail;
        public final String healthyPercentageFail;
        public final String permissionFail;

        private Messages(ConfigurationSection messagesConfig) {
            catchChanceSuccess = messagesConfig.getString("CatchChanceSuccess");
            catchChanceFail = messagesConfig.getString("CatchChanceFail");
            vaultSuccess = messagesConfig.getString("VaultSuccess");
            vaultFail = messagesConfig.getString("VaultFail");
            itemCostSuccess = messagesConfig.getString("ItemCostSuccess");
            itemCostFail = messagesConfig.getString("ItemCostFail");
            healthyPercentageFail = messagesConfig.getString("HealthyPercentageFail");
            permissionFail = messagesConfig.getString("PermissionFail");
        }
    }

    /**
     * Item cost config section
     */
    public static class ItemCost {
        public final int id;
        public final int data;
        public final Map<String, Integer> amounts;

        private ItemCost(ConfigurationSection itemCostConfig) {
            id = itemCostConfig.getInt("ItemId", 266);
            data = itemCostConfig.getInt("ItemData", 0);

            ConfigurationSection amountsConfig = itemCostConfig.getConfigurationSection("Amount");
            amounts = configSectionToMap(amountsConfig, amountsConfig::getInt, 0);
        }
    }
}

package io.github.thebusybiscuit.cscorelib2.protection.modules;

import com.plotsquared.core.location.Location;
import com.plotsquared.core.permissions.Permission;
import com.plotsquared.core.player.PlotPlayer;
import com.plotsquared.core.plot.Plot;
import com.plotsquared.core.util.Permissions;
import io.github.thebusybiscuit.cscorelib2.protection.ProtectableAction;
import io.github.thebusybiscuit.cscorelib2.protection.ProtectionModule;
import lombok.NonNull;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.plugin.Plugin;

public class PlotSquared6ProtectionModule implements ProtectionModule {

    private final Plugin plugin;

    public PlotSquared6ProtectionModule(@NonNull Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public Plugin getPlugin() {
        return plugin;
    }

    @Override
    public void load() {
        // We don't need to load any APIs, everything is static
    }

    @Override
    public boolean hasPermission(OfflinePlayer p, org.bukkit.Location l, ProtectableAction action) {
        Block b = l.getBlock();

        Location location = Location.at(b.getWorld().getName(), b.getX(), b.getY(), b.getZ());

        if (location.isPlotRoad()) {
            return check(p, action);
        }

        Plot plot = location.getOwnedPlot();
        return plot == null || plot.isAdded(p.getUniqueId()) || check(p, action);
    }

    private boolean check(OfflinePlayer p, ProtectableAction action) {
        PlotPlayer<OfflinePlayer> player = PlotPlayer.from(p);
        switch (action) {
            case INTERACT_BLOCK:
                return Permissions.hasPermission(player, Permission.PERMISSION_ADMIN_INTERACT_UNOWNED);
            case ATTACK_PLAYER:
                return Permissions.hasPermission(player, Permission.PERMISSION_ADMIN_PVP);
            case PLACE_BLOCK:
            default:
                return Permissions.hasPermission(player, Permission.PERMISSION_ADMIN_BUILD_UNOWNED);
        }
    }
}
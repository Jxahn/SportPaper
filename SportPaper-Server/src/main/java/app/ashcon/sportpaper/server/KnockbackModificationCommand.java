package app.ashcon.sportpaper.server;

import com.google.common.collect.ImmutableList;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.util.StringUtil;
import org.github.paperspigot.PaperSpigotConfig;

import java.util.ArrayList;
import java.util.List;

public class KnockbackModificationCommand extends Command {

    private static final List<String> ARGUMENTS = ImmutableList.of("friction", "horizontal", "vertical", "vertical-limit", "extra-horizontal", "extra-vertical", "reset", "show");

    // Default values
    private final double knockbackHorizontal, knockbackVertical, knockbackRangeFactor, knockbackMaxRangeReduction, knockbackStartRange, knockbackIdleReduction;

    public KnockbackModificationCommand(String name, double knockbackHorizontal,
                                        double knockbackVertical, double knockbackRangeFactor, double knockbackMaxRangeReduction, double knockbackStartRange, double knockbackIdleReduction) {
        super(name);
        this.description = "Modify the knockback configuration";
        this.usageMessage = "<horizontal multiplier> <vertical multiplier> <range factor> <max range reduction> <start range> <idle reduction>";
        this.setPermission("bukkit.command.knockback");
        this.knockbackHorizontal = knockbackHorizontal;
        this.knockbackVertical = knockbackVertical;
        this.knockbackRangeFactor = knockbackRangeFactor;
        this.knockbackMaxRangeReduction = knockbackMaxRangeReduction;
        this.knockbackStartRange  = knockbackStartRange;
        this.knockbackIdleReduction = knockbackIdleReduction;
    }

    @Override
    public boolean execute(CommandSender sender, String currentAlias, String[] args) {
        if (!testPermission(sender)) {
            return true;
        }
        if (args.length < 1) {
            sender.sendMessage(ChatColor.WHITE + usageMessage);
            sender.sendMessage(ChatColor.YELLOW + "Current knockback values: " + ChatColor.LIGHT_PURPLE + PaperSpigotConfig.knockbackHorizontal + ChatColor.YELLOW + ", " + ChatColor.LIGHT_PURPLE + PaperSpigotConfig.knockbackVertical + ChatColor.YELLOW + ", " + ChatColor.LIGHT_PURPLE + PaperSpigotConfig.knockbackRangeFactor + ChatColor.YELLOW + ", " + ChatColor.LIGHT_PURPLE + PaperSpigotConfig.knockbackMaxRangeReduction + ChatColor.YELLOW + ", " + ChatColor.LIGHT_PURPLE + PaperSpigotConfig.knockbackStartRange + ChatColor.YELLOW + " and " + ChatColor.LIGHT_PURPLE + PaperSpigotConfig.knockbackIdleReduction + ChatColor.YELLOW + ".");
            return true;
        }


        try {
            switch (args[0].toLowerCase()) {
                case "horizontal": {
                    double oldVal = PaperSpigotConfig.knockbackHorizontal;
                    double newVal = parseValue(args);
                    PaperSpigotConfig.knockbackHorizontal = newVal;
                    updated(sender, "horizontal knockback", oldVal, newVal);
                    break;
                }
                case "vertical": {
                    double oldVal = PaperSpigotConfig.knockbackVertical;
                    double newVal = parseValue(args);
                    PaperSpigotConfig.knockbackVertical = newVal;
                    updated(sender, "vertical knockback", oldVal, newVal);
                    break;
                }
                case "rangefactor": {
                    double oldVal = PaperSpigotConfig.knockbackRangeFactor;
                    double newVal = parseValue(args);
                    PaperSpigotConfig.knockbackRangeFactor = newVal;
                    updated(sender, "knockback range-factor", oldVal, newVal);
                    break;
                }
                case "maxrangereduction": {
                    double oldVal = PaperSpigotConfig.knockbackMaxRangeReduction;
                    double newVal = parseValue(args);
                    PaperSpigotConfig.knockbackMaxRangeReduction = newVal;
                    updated(sender, "knockback max-range-reduction", oldVal, newVal);
                    break;
                }
                case "startrange": {
                    double oldVal = PaperSpigotConfig.knockbackStartRange;
                    double newVal = parseValue(args);
                    PaperSpigotConfig.knockbackStartRange = newVal;
                    updated(sender, "knockback start-range", oldVal, newVal);
                    break;
                }
                case "idlereduction": {
                    double oldVal = PaperSpigotConfig.knockbackIdleReduction;
                    double newVal = parseValue(args);
                    PaperSpigotConfig.knockbackIdleReduction = newVal;
                    updated(sender, "knockback idle-reduction", oldVal, newVal);
                    break;
                }
                case "show":
                    sender.sendMessage(ChatColor.GOLD + "Knockback Configuration");
                    sendValue(sender, "Horizontal Knockback", PaperSpigotConfig.knockbackHorizontal);
                    sendValue(sender, "Vertical Knockback", PaperSpigotConfig.knockbackVertical);
                    sendValue(sender, "Knockback Range Factor", PaperSpigotConfig.knockbackRangeFactor);
                    sendValue(sender, "Knockback Max Range Reduction", PaperSpigotConfig.knockbackMaxRangeReduction);
                    sendValue(sender, "Knockback Start Range", PaperSpigotConfig.knockbackStartRange);
                    sendValue(sender, "Knockback Idle Reduction", PaperSpigotConfig.knockbackIdleReduction);
                    break;
                default:
                    sender.sendMessage(ChatColor.RED + "Usage: " + usageMessage);
            }
        } catch (RuntimeException ex) {
            sender.sendMessage(ChatColor.RED + ex.getMessage());
        }

        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        if (args.length == 1) {
            return StringUtil.copyPartialMatches(args[0], ARGUMENTS, new ArrayList<>(ARGUMENTS.size()));
        }
        return ImmutableList.of();
    }

    private double parseValue(String[] args) {
        if (args.length != 2)
            throw new RuntimeException("Please specify a single value to set the option to.");

        try {
            return Double.parseDouble(args[1]);
        } catch (NumberFormatException ex) {
            throw new RuntimeException("Invalid value specified!");
        }
    }

    private void updated(CommandSender viewer, String name, double oldVal, double newVal) {
        viewer.sendMessage(ChatColor.GREEN + "Updated " + ChatColor.GOLD + name + ChatColor.GREEN + " from " +
                ChatColor.BLUE + oldVal + ChatColor.GREEN + " to " + ChatColor.BLUE + newVal);
    }

    private void sendValue(CommandSender viewer, String name, double value) {
        viewer.sendMessage(ChatColor.AQUA + ChatColor.BOLD.toString() + name + ChatColor.RESET +
                ": " + ChatColor.BLUE + value);
    }
}

public class SafeHouse extends NormalLoc{

    public SafeHouse(Player player) {
        super(player, "Güvenli Ev");
    }

    @Override
    public boolean getLocation() {
        player.setHealthy(player.getrHealthy());
        System.out.println("Revocering..."+" canınız fullendi-> "+player.getHealthy());
        System.out.println("Şuan güvenli evdesiniz");

        return true ;

    }
}

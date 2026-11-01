public abstract class BattleLoc extends Location {

   protected Obstcale obstcale ;
   protected String award;
    public BattleLoc(Player player,String name,Obstcale obstcale,String award) {
        super(player);
        this.name=name ;
        this.obstcale=obstcale;
        this.award=award;
         }

        public boolean getLocation(){

            int obsCount =obstcale.count();
            System.out.println("Şuan buradasınız: "+this.getName());
            System.out.println("Dikkatli ol! Burada "+obsCount+ " tane "+obstcale.getName()+" yaşıyor");
            System.out.print("<S>avaş veya <K>aç :");
            String selCase =  scan.nextLine();
            selCase=selCase.toUpperCase();
            if(selCase.equals("S")){
                if(combat(obsCount)){
                    System.out.println(this.getName()+" bölgesindeki tüm düşmanları temizlediniz");
                    if(this.award.equals("Food")&&player.getInv().isFood()==false){
                        System.out.println(this.award+" kazandınız ");
                        player.getInv().setFood(true);
                    }
                    else if(this.award.equals("Water")&&player.getInv().isWater()==false){
                        System.out.println(this.award+" kazandınız ");
                        player.getInv().setWater(true);
                    }
                    else if(this.award.equals("Firewood")&&player.getInv().isFirewood()==false){
                        System.out.println(this.award+" kazandınız ");
                        player.getInv().setFirewood(true);
                    }
                    return true;

                }if(player.getHealthy()<=0){
                    System.out.println("öldünüz");
                    return false;


                }


            }

            return true ;

        }
        public boolean combat(int obsCount){
                for(int i=0;i<obsCount;i++) {
                    int defObsHealth=obstcale.getHealth();
                    playerStats();
                    enemyStats();
                    System.out.println("-------------------");
                    while (player.getHealthy() > 0 && obstcale.getHealth() > 0) {

                        System.out.print("<V>ur veya <K>aç: ");
                        String selCase = scan.nextLine();
                        selCase = selCase.toUpperCase();
                        if (selCase.equals("V")) {

                            System.out.println("Siz vurdunuz !");
                            obstcale.setHealth(obstcale.getHealth() - player.getTotalDamage());
                            afterHit();
                            if (obstcale.getHealth() > 0) {

                                System.out.println("Canavar size vurdu");
                                player.setHealthy(player.getHealthy() - (obstcale.getDamage() - player.getInv().getArmor()));
                                afterHit();
                            }


                        } else {
                            return false;
                        }


                    }
                    if(obstcale.getHealth()<=0&&player.getHealthy()>0){
                        System.out.println("DüşmanI yendiniz ");
                        player.setMoney(player.getMoney()+obstcale.getAward());
                        System.out.println("Güncel Paranız: "+ player.getMoney());
                        obstcale.setHealth(defObsHealth);

                    }else {return false;}


                }
            return true;

        }
public void afterHit(){
    System.out.println("----------------------------------------");
    System.out.println("Oyuncu Canı: "+player.getHealthy());
    System.out.println(obstcale.getName()+" Canı: "+obstcale.getHealth());

}
public void playerStats(){


    System.out.println("------Oyuncu bilgileri-------");
    System.out.println("Can  : "+ player.getHealthy());
    System.out.println("Hasar: "+player.getTotalDamage());
    System.out.println("Para : "+player.getMoney());
    if(player.getInv().getDamage()>0){

        System.out.println("Silah: "+player.getInv().getwName());

    }
    if(player.getInv().getArmor()>0){

        System.out.println("Zırh : "+player.getInv().getaName());

    }
}
        public void enemyStats(){

            System.out.println("------------"+obstcale.getName()+" değerleri------------ ");
            System.out.println("Can  : "+obstcale.getHealth());
            System.out.println("Hasar: "+obstcale.getDamage());
            System.out.println("Ödül : "+obstcale.getAward());

        }

}

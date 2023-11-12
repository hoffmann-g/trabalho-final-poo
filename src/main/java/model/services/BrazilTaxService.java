package model.services;

public class BrazilTaxService implements TaxService{

    @Override
    public Double tax(Double amount) {
        if(amount > 100){
            return amount * 0.15;
        } else {
            return amount * 0.2;
        }
    }
}

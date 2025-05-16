package store.account;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends CrudRepository<AccountModel, String> {

    public AccountModel findByEmailAndSha256(String email, String sha256);
    
}

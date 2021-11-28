package ru.pcs.crowdfunding.client.services;

import ru.pcs.crowdfunding.client.dto.SignUpForm;

import java.util.Locale;

@Service
@RequiredArgsConstructor
public class SignUpServiceImpl implements SignUpService {
    private final ClientsRepository clientsRepository;

    @Override
    public void signUp(SignUpForm form) {
         Client client = Client.builder()
                .firstName(form.getFirstName())
                .lastName(form.getLastName())
                .country(form.getCountry())
                .city(form.getCity())
                .email(form.getEmail().toLowerCase(Locale.ROOT))
                .password(form.getPassword())
                .build();

        clientsRepository.save(client);
    }
}

package code.ecommerceproject.service;

import code.ecommerceproject.entity.Address;
import code.ecommerceproject.repository.AddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AddressService {

    private final AddressRepository addressRepository;

    @Transactional
    public Address createAddress(final Address address) {

        if (address.getUser() != null) {
            // Check if an address with the same userId already exists
            final Optional<Address> existingAddress = addressRepository.findByUserId(address.getUser().getId());
            if (existingAddress.isPresent()) {
                return existingAddress.get();
            }
        }

        return addressRepository.save(address);
    }

    public Optional<Address> getAddressById(final UUID id) {
        return addressRepository.findById(id);
    }

    @Transactional
    public Address updateAddress(final UUID id, final Address addressDetails) {
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Address not found with id " + id));
        address.setStreet(addressDetails.getStreet());
        address.setCity(addressDetails.getCity());
        address.setCountry(address.getCountry());
        address.setZipCode(addressDetails.getZipCode());
        return addressRepository.save(address);
    }

    public void deleteAddress(final UUID id) {
        addressRepository.deleteById(id);
    }
} 
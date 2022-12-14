package org.practice.dev.msglmelipracticetherevenge.service;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.practice.dev.msglmelipracticetherevenge.dto.ipaddressblacklist.GetIpBlacklistDto;
import org.practice.dev.msglmelipracticetherevenge.dto.ipaddressblacklist.MapperBlacklist;
import org.practice.dev.msglmelipracticetherevenge.exception.IpAddressIsBannedException;
import org.practice.dev.msglmelipracticetherevenge.model.IpAddressBlacklist;
import org.practice.dev.msglmelipracticetherevenge.repository.IpAddressBlacklistRepo;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;

@Log4j2
@Service
@AllArgsConstructor
public class IpAddressBlacklistServiceImpl implements IpAddressBlacklistService {
    private final IpAddressBlacklistRepo ipAddressBlacklistRepository;
    private final MapperBlacklist mapperBlacklist;

    @Override
    public GetIpBlacklistDto banIpAddress(String ipAddress) {
        if (ipAddressIsBaned(ipAddress)) {
            throw new IpAddressIsBannedException("The ip address: " + ipAddress + " is already banned.");
        }
        IpAddressBlacklist ipAddressBlacklistModel = IpAddressBlacklist.builder()
                .ipAddress(ipAddress)
                .banDateActivated(Timestamp.from(Instant.now()))
                .banActivate(Boolean.TRUE)
                .build();
        ipAddressBlacklistRepository.save(ipAddressBlacklistModel);
        return mapperBlacklist.toGetIpBlacklistDto(ipAddressBlacklistModel);

    }

    @Override
    public void unbanIpAddress(String ipAddress) {

    }

    @Override
    public Boolean ipAddressIsBaned(String ipAddress) {
        return ipAddressBlacklistRepository.findByIpAddressAndBanActivate(ipAddress, Boolean.TRUE).isPresent();
    }
}

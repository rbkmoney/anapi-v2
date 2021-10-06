package com.rbkmoney.anapi.v2.security;

import com.rbkmoney.anapi.v2.config.properties.BouncerProperties;
import com.rbkmoney.anapi.v2.service.KeycloakService;
import com.rbkmoney.anapi.v2.service.OrgMgmtService;
import com.rbkmoney.bouncer.base.Entity;
import com.rbkmoney.bouncer.context.v1.*;
import com.rbkmoney.bouncer.ctx.ContextFragmentType;
import com.rbkmoney.bouncer.decisions.Context;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.thrift.TSerializer;
import org.keycloak.representations.AccessToken;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class BouncerContextFactory {

    private final BouncerProperties bouncerProperties;
    private final OrgMgmtService orgMgmtService;
    private final KeycloakService keycloakService;

    @SneakyThrows
    public Context buildContext(AnapiBouncerContext bouncerContext) {
        ContextFragment contextFragment = buildContextFragment(bouncerContext);
        var serializer = new TSerializer();
        var fragment = new com.rbkmoney.bouncer.ctx.ContextFragment()
                .setType(ContextFragmentType.v1_thrift_binary)
                .setContent(serializer.serialize(contextFragment));

        com.rbkmoney.bouncer.ctx.ContextFragment
                userFragment = orgMgmtService.getUserAuthContext(keycloakService.getAccessToken().getSubject());
        Context context = new Context();
        context.putToFragments(bouncerProperties.getContextFragmentId(), fragment);
        context.putToFragments("user", userFragment);
        return context;
    }

    private ContextFragment buildContextFragment(AnapiBouncerContext bouncerContext) {
        Environment env = buildEnvironment();
        AccessToken accessToken = keycloakService.getAccessToken();
        ContextAnalyticsAPI contextAnalyticsApi = buildAnapiContext(bouncerContext);
        return new ContextFragment()
                .setAuth(buildAuth(bouncerContext, accessToken))
                .setEnv(env)
                .setAnapi(contextAnalyticsApi);
    }

    private Auth buildAuth(AnapiBouncerContext bouncerContext, AccessToken accessToken) {
        Auth auth = new Auth();
        Set<AuthScope> authScopeSet = bouncerContext.getShopIds().stream()
                .map(shopId -> new AuthScope()
                        .setParty(new Entity().setId(bouncerContext.getPartyId()))
                        .setShop(new Entity().setId(shopId)))
                .collect(Collectors.toSet());
        return auth.setToken(new Token().setId(accessToken.getId()))
                .setMethod(bouncerProperties.getAuthMethod())
                .setExpiration(Instant.ofEpochSecond(accessToken.getExp()).toString())
                .setScope(authScopeSet);
    }

    private Environment buildEnvironment() {
        Deployment deployment = new Deployment()
                .setId(bouncerProperties.getDeploymentId());
        return new Environment()
                .setDeployment(deployment)
                .setNow(Instant.now().toString());
    }

    private ContextAnalyticsAPI buildAnapiContext(AnapiBouncerContext ctx) {
        return new ContextAnalyticsAPI()
                .setOp(new AnalyticsAPIOperation()
                        .setId(ctx.getOperationId()));
    }

}

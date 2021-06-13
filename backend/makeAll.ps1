try {
    'commons' | Set-Location
    & mvn @('clean', 'install')

    @(
        'inventoryService',
        'logisticsService',
        'menuService',
        'orderService',
        'serviceRegistry',
        'apiGatewayService'
    ) | ForEach-Object {
        "./../$_" | Set-Location
        & mvn @('-B', '-DskipTests', 'package', 'spring-boot:repackage')
    }
}
finally {
    $PSScriptRoot | Set-Location
}
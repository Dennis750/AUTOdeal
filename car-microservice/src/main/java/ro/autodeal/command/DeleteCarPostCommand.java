package ro.autodeal.command;

import ro.autodeal.service.CarPostCommandService;

public class DeleteCarPostCommand implements CarPostCommand {

    private final CarPostCommandService carPostCommandService;
    private final Long id;

    public DeleteCarPostCommand(CarPostCommandService carPostCommandService,
                                Long id) {
        this.carPostCommandService = carPostCommandService;
        this.id = id;
    }

    @Override
    public void execute() {
        carPostCommandService.deleteById(id);
    }
}
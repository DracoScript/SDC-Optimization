import matplotlib.pyplot as plt
import os
import sys
import numpy as np

# check if dir was provided

if (len(sys.argv) < 1):
    print 'missing parameter directory'
    sys.exit()

reverse = 0
if (len(sys.argv) > 2):
    reverse = sys.argv[2]



# List all files from input path

mypath = sys.argv[1]
onlyfiles = [f for f in os.listdir(mypath) if os.path.isfile(os.path.join(mypath, f))]
print 'Loading '+str(len(onlyfiles))+' files...'
print ''



# Create lists

sa0_gen = []
sa0_best = []
sa0_sol = []
sa0_timer = []
sa1_gen = []
sa1_best = []
sa1_sol = []
sa1_timer = []
sa2_gen = []
sa2_best = []
sa2_sol = []
sa2_timer = []



# Read files

for file_name in onlyfiles:

    if (file_name.find('.stat') < 0):
        continue;

    # Read data
    data = open(mypath+file_name)

    gen = []
    best = []
    sol = "";
    timer = []

    for line in data:
        line = line.replace('\n', '')
        aux = line.split(' ')
        gen.append(aux[0])
        timer.append(aux[len(aux)-1])
        if (reverse):
            best.append(-aux[1])
        else:
            best.append(aux[1])
        sol = aux[2]

    # Store in Simulated Annealing lists
    if (file_name.find('0_SA.stat') > 0):

        sa0_gen.append(gen)
        sa0_best.append(best)
        sa0_sol.append(sol)
        sa0_timer.append(timer)

    elif (file_name.find('1_SA.stat') > 0):

        sa1_gen.append(gen)
        sa1_best.append(best)
        sa1_sol.append(sol)
        sa1_timer.append(timer)

    elif (file_name.find('2_SA.stat') > 0):

        sa2_gen.append(gen)
        sa2_best.append(best)
        sa2_sol.append(sol)
        sa2_timer.append(timer)



# Convert all lists to arrays
sa0_best = np.asarray(sa0_best, 'int')
sa0_gen = np.asarray(sa0_gen, 'int')
sa0_timer = np.asarray(sa0_timer, 'int')
sa1_best = np.asarray(sa1_best, 'int')
sa1_gen = np.asarray(sa1_gen, 'int')
sa1_timer = np.asarray(sa1_timer, 'int')
sa2_best = np.asarray(sa2_best, 'int')
sa2_gen = np.asarray(sa2_gen, 'int')
sa2_timer = np.asarray(sa2_timer, 'int')

# Statistics
sa0_mean = np.mean(sa0_best[:,-1])
sa0_std = np.std(sa0_best[:,-1])
sa0_min = np.min(sa0_best[:,-1])
sa0_max = np.max(sa0_best[:,-1])
sa0_fit = np.argmin(sa0_best[:,-1])
sa0_meant = np.mean(sa0_timer[:,-1])
sa0_stdt = np.std(sa0_timer[:,-1])
sa0_mint = np.min(sa0_timer[:,-1])
sa0_maxt = np.max(sa0_timer[:,-1])
sa1_mean = np.mean(sa1_best[:,-1])
sa1_std = np.std(sa1_best[:,-1])
sa1_min = np.min(sa1_best[:,-1])
sa1_max = np.max(sa1_best[:,-1])
sa1_fit = np.argmin(sa1_best[:,-1])
sa1_meant = np.mean(sa1_timer[:,-1])
sa1_stdt = np.std(sa1_timer[:,-1])
sa1_mint = np.min(sa1_timer[:,-1])
sa1_maxt = np.max(sa1_timer[:,-1])
sa2_mean = np.mean(sa2_best[:,-1])
sa2_std = np.std(sa2_best[:,-1])
sa2_min = np.min(sa2_best[:,-1])
sa2_max = np.max(sa2_best[:,-1])
sa2_fit = np.argmin(sa2_best[:,-1])
sa2_meant = np.mean(sa2_timer[:,-1])
sa2_stdt = np.std(sa2_timer[:,-1])
sa2_mint = np.min(sa2_timer[:,-1])
sa2_maxt = np.max(sa2_timer[:,-1])



# PLOT Best Solution
plt.subplot(2,2,1)
for i in range(len(sa0_best)):
    plt.plot(sa0_gen[i], sa0_best[i], '-', color='lightblue', alpha=0.2)
for i in range(len(sa1_best)):
    plt.plot(sa1_gen[i], sa1_best[i], '-', color='indianred', alpha=0.2)
for i in range(len(sa2_best)):
    plt.plot(sa2_gen[i], sa2_best[i], '-', color='greenyellow', alpha=0.2)
# Best Solution
plt.plot(sa0_gen[sa0_fit], sa0_best[sa0_fit], '-', color='white', alpha=1, linewidth=5)
plt.plot(sa0_gen[sa0_fit], sa0_best[sa0_fit], '-', color='blue', alpha=0.75, linewidth=3)
plt.plot(sa1_gen[sa1_fit], sa1_best[sa1_fit], '-', color='white', alpha=1, linewidth=5)
plt.plot(sa1_gen[sa1_fit], sa1_best[sa1_fit], '-', color='red', alpha=0.75, linewidth=3)
plt.plot(sa2_gen[sa2_fit], sa2_best[sa2_fit], '-', color='white', alpha=1, linewidth=5)
plt.plot(sa2_gen[sa2_fit], sa2_best[sa2_fit], '-', color='green', alpha=0.75, linewidth=3)
# Plot details
plt.ylim(0, 2 * (sa0_mean + sa1_mean + sa2_mean) / 3)
plt.xlabel('generation')
plt.ylabel('fitness of best solution (days)')
leg = plt.legend(['SA -1', 'SA inf', 'SA 1'])
leg.legendHandles[0].set_color('blue')
leg.legendHandles[0].set_alpha(1)
leg.legendHandles[1].set_color('red')
leg.legendHandles[1].set_alpha(1)
leg.legendHandles[2].set_color('green')
leg.legendHandles[2].set_alpha(1)

# PLOT Best Solution Statistics
plt.subplot(2,2,2)
# Plot details
plt.ylim(0, 2 * (sa0_mean + sa1_mean + sa2_mean) / 3)
box = plt.boxplot([sa0_best[:,-1], sa1_best[:,-1], sa2_best[:,-1]], vert=True, patch_artist=True, widths=0.5)
box['boxes'][0].set_facecolor('lightblue')
box['boxes'][1].set_facecolor('indianred')
box['boxes'][2].set_facecolor('greenyellow')
leg = plt.legend(['SA -1', 'SA inf', 'SA 1'])
leg.legendHandles[0].set_marker('s')
leg.legendHandles[0].set_color('lightblue')
leg.legendHandles[1].set_marker('s')
leg.legendHandles[1].set_color('indianred')
leg.legendHandles[2].set_marker('s')
leg.legendHandles[2].set_color('greenyellow')


# PLOT Timer
plt.subplot(2,2,3)
for i in range(len(sa0_timer)):
    plt.plot(sa0_gen[i], sa0_timer[i], '-', color='lightblue', alpha=0.2)
for i in range(len(sa1_timer)):
    plt.plot(sa1_gen[i], sa1_timer[i], '-', color='indianred', alpha=0.2)
for i in range(len(sa2_timer)):
    plt.plot(sa2_gen[i], sa2_timer[i], '-', color='greenyellow', alpha=0.2)
# Best Solution Timer
plt.plot(sa0_gen[sa0_fit], sa0_timer[sa0_fit], '-', color='white', alpha=1, linewidth=5)
plt.plot(sa0_gen[sa0_fit], sa0_timer[sa0_fit], '-', color='blue', alpha=0.75, linewidth=3)
plt.plot(sa1_gen[sa1_fit], sa1_timer[sa1_fit], '-', color='white', alpha=1, linewidth=5)
plt.plot(sa1_gen[sa1_fit], sa1_timer[sa1_fit], '-', color='red', alpha=0.75, linewidth=3)
plt.plot(sa2_gen[sa2_fit], sa2_timer[sa2_fit], '-', color='white', alpha=1, linewidth=5)
plt.plot(sa2_gen[sa2_fit], sa2_timer[sa2_fit], '-', color='green', alpha=0.75, linewidth=3)
# Plot details
plt.ylim(0, 2 * (sa0_meant + sa1_meant + sa2_meant) / 3)
plt.xlabel('generation')
plt.ylabel('time spent (ns)')
leg = plt.legend(['SA -1', 'SA inf', 'SA 1'])
leg.legendHandles[0].set_color('blue')
leg.legendHandles[0].set_alpha(1)
leg.legendHandles[1].set_color('red')
leg.legendHandles[1].set_alpha(1)
leg.legendHandles[2].set_color('green')
leg.legendHandles[2].set_alpha(1)

# PLOT Timer Statistics
plt.subplot(2,2,4)
# Plot details
plt.ylim(0, 2 * (sa0_meant + sa1_meant + sa2_meant) / 3)
box = plt.boxplot([sa0_timer[:,-1], sa1_timer[:,-1], sa2_timer[:,-1]], vert=True, patch_artist=True, widths=0.5)
box['boxes'][0].set_facecolor('lightblue')
box['boxes'][1].set_facecolor('indianred')
box['boxes'][2].set_facecolor('greenyellow')
leg = plt.legend(['SA -1', 'SA inf', 'SA 1'])
leg.legendHandles[0].set_marker('s')
leg.legendHandles[0].set_color('lightblue')
leg.legendHandles[1].set_marker('s')
leg.legendHandles[1].set_color('indianred')
leg.legendHandles[2].set_marker('s')
leg.legendHandles[2].set_color('greenyellow')

# Print results before showing plot
print ''
print '>>> RESULTS - Mission Duration (days) <<<'
print ''
print 'Simulated Annealing - Tweak at Random:'
print '    Best Solution      = '+str(sa0_best[sa0_fit][-1])+" "+sa0_sol[sa0_fit]
print '    Mean of Solutions  = '+str(sa0_mean)+" ["+str(sa0_min)+"-"+str(sa0_max)+"]"
print '    Standard Deviation = '+str(sa0_std)
print ''
print 'Simulated Annealing - Tweak to Best:'
print '    Best Solution      = '+str(sa1_best[sa1_fit][-1])+" "+sa1_sol[sa1_fit]
print '    Mean of Solutions  = '+str(sa1_mean)+" ["+str(sa1_min)+"-"+str(sa1_max)+"]"
print '    Standard Deviation = '+str(sa1_std)
print ''
print 'Simulated Annealing - Tweak to First:'
print '    Best Solution      = '+str(sa2_best[sa2_fit][-1])+" "+sa2_sol[sa2_fit]
print '    Mean of Solutions  = '+str(sa2_mean)+" ["+str(sa2_min)+"-"+str(sa2_max)+"]"
print '    Standard Deviation = '+str(sa2_std)
print ''
print ''
print '>>> RESULTS - Optimization Time (ns) <<<'
print ''
print 'Simulated Annealing - Tweak at Random:'
print '    Best Solution      = '+str(sa0_timer[sa0_fit][-1])
print '    Mean of Solutions  = '+str(sa0_meant)+" ["+str(sa0_mint)+"-"+str(sa0_maxt)+"]"
print '    Standard Deviation = '+str(sa0_stdt)
print ''
print 'Simulated Annealing - Tweak to Best:'
print '    Best Solution      = '+str(sa1_timer[sa1_fit][-1])
print '    Mean of Solutions  = '+str(sa1_meant)+" ["+str(sa1_mint)+"-"+str(sa1_maxt)+"]"
print '    Standard Deviation = '+str(sa1_stdt)
print ''
print 'Simulated Annealing - Tweak to First:'
print '    Best Solution      = '+str(sa2_timer[sa2_fit][-1])
print '    Mean of Solutions  = '+str(sa2_meant)+" ["+str(sa2_mint)+"-"+str(sa2_maxt)+"]"
print '    Standard Deviation = '+str(sa2_stdt)
print ''

# PLOT Save
plt.savefig(mypath[0:-1]+'_plot.png')

# PLOT Show
#plt.show()